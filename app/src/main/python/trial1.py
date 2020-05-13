import tensorflow as tf
from os.path import dirname,join
from tensorflow.keras.preprocessing.text import Tokenizer #new
from tensorflow.keras.preprocessing.sequence import pad_sequences
import pandas as pd
def trial():
    model=tf.keras.models.load_model(join(dirname(__file__),'MyFakeNews.h5'))
    test = pd.DataFrame(columns=['id','author','title','text'])
    uid=[1]
    author=['Divine Revelation Of Heaven & Hell']
    title=['Death row inmate eats 1200 pages of the Bible as his last meal.']
    text=['A condemned murderer, Jeremy Morris from Atmore Alabama has reportedly eaten 1200 pages of the Holy bible he requested from a prison guard.The 33-year-old and murderer of two catholic nuns requested the bible as his last meal.According to Worldnews, the prison guards of the William C. Holman Correctional Facility, thought he wanted to pray and repent, so they gave him an old copy of the King James Bible.But surprisingly, the condemned murderer took hours to tear the 1200-page holy book including its cover into small pieces before he ate them.The prison guard, Walter Henri, who handed him the old and worn out Bible, says the inmate seemed to “savor every bite.”“He kept tearing pieces from his Bible and eating them like they were potato chips!”']
    test['id']=uid
    test['author']=author
    test['title']=title
    test['text']=text
    test_data = test.copy()
    test_data = test_data.set_index('id', drop = True)
    test_data = test_data.fillna(' ')
    max_features=4500
    tokenizer = Tokenizer(num_words = max_features, filters='!"#$%&()*+,-./:;<=>?@[\\]^_`{|}~\t\n', lower = True, split = ' ')
    tokenizer.fit_on_texts(texts = test_data['text'])
    test_text = tokenizer.texts_to_sequences(texts = test_data['text'])
    test_text = pad_sequences(sequences = test_text, maxlen = max_features, padding = 'pre')
    lstm_prediction = model.predict(test_text)
    '''submission = pd.DataFrame(columns=['id','label'])
    submission['id'] = test_data.index
    submission['label'] = lstm_prediction'''
    return test['author'][0]
