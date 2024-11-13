from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
#type:ignore
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///calmconnect.db'
db = SQLAlchemy(app)

class Resource(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100))
    description = db.Column(db.Text)
    type = db.Column(db.String(50))
    imageUrl = db.Column(db.String(255))
    createdAt = db.Column(db.DateTime)
    ratings = db.relationship('Rating', backref='resource', lazy=True)

    def __repr__(self):
        return f'<Resource {self.id}>'

class Rating(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    resource_id = db.Column(db.Integer, db.ForeignKey('resource.id'))
    rating = db.Column(db.Integer)
    comment = db.Column(db.Text)

    def __repr__(self):
        return f'<Rating {self.id}>'

# ResourceController and RatingController similar to Java