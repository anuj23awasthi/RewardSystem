const express = require("express");
const router = express.Router();

const {
    getTodayLeaderboard,
    getGlobalLeaderboard
} = require("../controllers/leaderboardController");

// TODAY leaderboard
router.get("/today", getTodayLeaderboard);

// GLOBAL leaderboard
router.get("/global", getGlobalLeaderboard);

module.exports = router;
