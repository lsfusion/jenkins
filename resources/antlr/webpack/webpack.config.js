const path = require('path');

module.exports = {
    mode: 'production',
    entry: './index.js',
    output: {
        filename: 'antlrLsfJSLogics.js',
        path: path.resolve(__dirname, 'dist'),
    },
    resolve: {
        fallback: {
            fs: false
        }
    }
};