import re

class Probabilities:
    def __init__(self, message_counts, word_counts):
        self.message_counts = message_counts
        self.word_counts = word_counts
    
    def message_is_spam(self):
        total = sum(self.message_counts)
        return float(self.message_counts[0])/total
    
    def message_is_ham(self):
        return 1 - self.message_is_spam()
        
    def __word_counts(self, word):
        if self.word_counts.has_key(word):
            return self.word_counts[word]
        else:
            return (0,0)
    
    def increase_message_counts(self, spam_count = 0, ham_count = 0):
        old_counts = self.message_counts
        self.message_counts = (old_counts[0] + spam_count, old_counts[1] + ham_count)
    
    def increase_word_counts(self, word, spam_count = 0, ham_count = 0):
        old_counts = self.__word_counts(word)
        self.word_counts[word] = (old_counts[0] + spam_count, old_counts[1] + ham_count)
        
    def word_is_spam(self, word):
        total_spam_word_count = sum(map(lambda t: t[0], self.word_counts.values()))
        word_counts = self.__word_counts(word)
        
        return float(word_counts[0])/total_spam_word_count
    
    def word_is_ham(self, word):
        total_ham_word_count = sum(map(lambda t: t[1], self.word_counts.values()))
        word_counts = self.__word_counts(word)
        
        
        return float(word_counts[1])/total_ham_word_count
    
    @staticmethod
    def read(filename):
        f = open(filename, "r")
        
        message_counts = (0,0)
        word_counts    = {}
        
        first_line = True
        for line in f.readlines():
            line = line.strip()
            
            if line == "":
                continue
            
            if first_line:
                count_spam, count_ham = map(lambda s: int(s), line.split(","))
                message_counts = (count_spam, count_ham, )
                first_line = False
            else:
                word, count_spam, count_ham = line.split(",")
                count_spam, count_ham = map(lambda s: int(s), [count_spam, count_ham])
                
                word_counts[word] = (count_spam, count_ham, )
        
        f.close()
        
        return Probabilities(message_counts, word_counts)
    
    def write(self, filename):
        f = open(filename, "w+")
        f.write( ",".join(map(lambda i: str(i), self.message_counts)) + "\n" )
        
        for word in self.word_counts.keys():
            counts = self.word_counts[word]
            f.write("%s,%s,%s\n" % (word, counts[0], counts[1]))
        
        f.write("\n")
        f.close()

class Message:
    def __init__(self, contents):
        self.contents = contents
        self.word_counts = {}
        
        for line in contents.splitlines():
            line = line.strip()
            if line == "": continue
            
            for word in re.split(' ', line):
                word = word.strip().upper()
                if word == "": continue
                
                if re.match('^[A-Z]*$', word):                    
                    if not self.word_counts.has_key(word):
                        self.word_counts[word] = 1
                    else:
                        self.word_counts[word] += 1
    
    def words(self):
        return self.word_counts.keys()
        
    @staticmethod
    def read(filename):
        f = open(filename, "r")
        contents = f.read()
        f.close()
        
        return Message(contents)    

def spamicity(message, probabilities):
    odds = probabilities.message_is_spam() / probabilities.message_is_ham()

    for word in message.words():
        p_spam = probabilities.word_is_spam(word)
        p_ham  = probabilities.word_is_ham(word)
        
        if p_spam == 0 and p_ham == 0:
            continue
        if p_spam == 0:
            odds = odds * (0.001/p_ham)
        elif p_ham == 0:
            odds = odds * (p_spam/0.001)
        else:
            odds = odds * (p_spam/p_ham)
                
    return (odds/(1+odds))

if __name__ == "__main__":
    import sys
    probs = Probabilities.read("pfile")
    
    if sys.argv[1] == "--teach":
        for i in range(2, len(sys.argv)):
            filename = sys.argv[i]
            message  = Message.read(filename)
        
            print message.contents
            print "\n\nDECISION TIME"
        
            while True:
                response = raw_input("[S]pam or [h]am: ")
            
                if response == 'S' or response == 'h':
                    break
                else:
                    print "Try again..."
                
            if response == 'S':
                probs.increase_message_counts(1, 0)
            else:
                probs.increase_message_counts(0, 1)
        
            for word in message.words():
                increase = message.word_counts[word]
                spam_increase = (response == 'S' and increase) or 0
                ham_increase = (response == 'h' and increase) or 0
            
                probs.increase_word_counts(word, spam_increase, ham_increase)
        
            print "---------------------------------------------------------------------\n\n\n\n"
    
        probs.write("pfile")
    else:
        message = Message.read(sys.argv[1])
        
        print message.contents
        print "Checking if it's spam..."
        spam_p = spamicity(message, probs)
        print "spamicity says %s" % spam_p
        
        if spam_p > 0.6:
            print "IT IS SPAM"
        else:
            print "it's just ham"