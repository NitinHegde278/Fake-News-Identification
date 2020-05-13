
from flask import Flask, request
from flask_restful import Resource, Api
import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.linear_model import LogisticRegression
from joblib import dump, load
from sklearn.pipeline import Pipeline

logreg = load('model.joblib')
pipeline = load('pipeline.joblib')

app = Flask(__name__)
api = Api(app)

class modelx(Resource):
    def post(self):
        test = pd.DataFrame(columns=['id','author','title','text'])
        uid = [1]
        author = [str(request.json['author'])]
        title = [str(request.json['title'])]
        #text = [str(request.json['text'])]
        test['id']=uid
        test['author']=author
        test['title']=title
        #test['text']=text
        test['label']='t'
        test=test.fillna(' ')
        #test['total']=test['title']+' '+test['author']+test['text']
        test['total']=test['title']+' '+test['author']
        test_tfidf = pipeline.transform(test['total'].values)

        predictions = logreg.predict(test_tfidf)
        pred=pd.DataFrame(predictions,columns=['label'])
        pred['id']=test['id']

        out = str(pred['label'][0])
        if(out == "0"):
            out = "Reliable!!"
        else:
            out = "Unreliable!"
        result = {"data" : out}
        return result

api.add_resource(modelx, '/')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug="true")
