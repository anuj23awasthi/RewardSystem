const express = require("express");
const router = express.Router();

const { getTodayChallenge, submitChallenge } = require("../controllers/challengeController");

router.get("/today", getTodayChallenge);
router.post("/submit", submitChallenge);

module.exports = router;
