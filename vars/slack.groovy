def message(message, channel) {
    if (channel != null) {
        slackSend color: 'good', message: message
    } else {
        slackSend channel: channel, color: 'good', message: message
    }
}