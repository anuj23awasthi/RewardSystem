const mongoose = require("mongoose");

const UserSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  tokens: { type: Number, default: 0 },
  streak: { type: Number, default: 0 },
  area: { type: String, default: "" },
  city: { type: String, default: "" },
  country: { type: String, default: "" }
});

module.exports = mongoose.model("User", UserSchema);
