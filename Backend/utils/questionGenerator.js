exports.generateDailyQuestion = () => {
    const a = Math.floor(Math.random() * 10) + 1; // 1–10
    const b = Math.floor(Math.random() * 10) + 1;

    return {
        question: `${a} × ${b} = ?`,
        correctAnswer: a * b
    };
};
