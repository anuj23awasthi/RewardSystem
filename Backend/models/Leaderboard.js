const mongoose = require("mongoose");

const LeaderboardSchema = new mongoose.Schema({
    user: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
    tokensEarned: { type: Number, default: 0 },
    correct: { type: Boolean, default: false },
    submitted: { type: Boolean, default: false },
    date: { type: String, required: true } // YYYY-MM-DD
});

// Improve query performance
LeaderboardSchema.index({ user: 1, date: 1 }, { unique: true });

module.exports = mongoose.model("Leaderboard", LeaderboardSchema);
