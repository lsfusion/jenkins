def message(String channel, String message) {
    slackSend channel: channel, color: 'good', message: message
}

