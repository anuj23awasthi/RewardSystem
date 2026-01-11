const mongoose = require("mongoose");

const ChallengeSchema = new mongoose.Schema({
    question: { type: String, required: true },
    correctAnswer: { type: Number, required: true },
    date: { type: String, required: true }
});

module.exports = mongoose.model("Challenge", ChallengeSchema);
