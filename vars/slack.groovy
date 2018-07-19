def message(message) {
    slackSend color: 'good', message: message
}

def message(message, channel) {
    slackSend channel: channel, color: 'good', message: message
}