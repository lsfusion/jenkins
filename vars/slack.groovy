def message(message) {
    slackSend color: 'good', message: message
}

def message(message, channel) {
    slackSend channel: channel, color: 'good', message: message
}

def warning(message) {
    slackSend color: 'warning', message: message
}

def warning(message, channel) {
    slackSend channel: channel, color: 'warning', message: message
}

def error(message) {
    slackSend color: 'danger', message: message
}

def error(message, channel) {
    slackSend channel: channel, color: 'danger', message: message
}

def send(message, color) {
    slackSend color: color, message: message
}

def send(message, color, channel) {
    slackSend channel: channel, color: color, message: message
}