const express = require("express");
const router = express.Router();

const {
    getAreaLeaderboard,
    getCityLeaderboard,
    getCountryLeaderboard
} = require("../controllers/locationLeaderboardController");

router.get("/area/:area", getAreaLeaderboard);
router.get("/city/:city", getCityLeaderboard);
router.get("/country/:country", getCountryLeaderboard);

module.exports = router;
