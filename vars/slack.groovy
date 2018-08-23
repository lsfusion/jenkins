def message(text) {
    if(Paths.slackChannel != null)
        message text, Paths.slackChannel
    else
        slackSend color: 'good', message: text
}

def message(text, channel) {
    slackSend channel: channel, color: 'good', message: text
}

def warning(message) {
    if(Paths.slackChannel != null)
        warning message, Paths.slackChannel
    else
        slackSend color: 'warning', message: message
}

def warning(message, channel) {
    slackSend channel: channel, color: 'warning', message: message
}

def error(message) {
    if(Paths.slackChannel != null)
        error message, Paths.slackChannel
    else
        slackSend color: 'danger', message: message
}

def error(message, channel) {
    slackSend channel: channel, color: 'danger', message: message
}