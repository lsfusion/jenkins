def message(channel, message) {
    slackSend channel: channel, color: 'good', message: message
}
