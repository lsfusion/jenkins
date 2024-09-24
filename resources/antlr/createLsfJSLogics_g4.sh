#!/bin/bash
#Preparing to clear the file
sed "s/'{'/'TMP_OPEN_BRACKET'/g;" $1 > tmp ; mv tmp LsfJSLogics.g4                                # change all '{' brackets to 'TMP_OPEN_BRACKET' to change back later
sed "s/'}'/'TMP_CLOSE_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                    # change all '}' brackets to 'TMP_CLOSE_BRACKET' to change back later

sed "s/'\['/'TMP_OPEN_SQUARE_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4             # change all '[' brackets to 'TMP_OPEN_SQUARE_BRACKET' to change back later
sed "s/'\]'/'TMP_CLOSE_SQUARE_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4            # change all ']' brackets to 'TMP_CLOSE_SQUARE_BRACKET' to change back later

sed "s/'('/'TMP_OPEN_ROUND_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4               # change all '(' brackets to 'TMP_OPEN_ROUND_BRACKET' to change back later
sed "s/')'/'TMP_CLOSE_ROUND_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4              # change all ')' brackets to 'TMP_CLOSE_ROUND_BRACKET' to change back later

sed "s/'(+)'/'TMP_PLUS_IN_ROUND_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4          # change all '(+)' to 'TMP_PLUS_IN_ROUND_BRACKET' to change back later
sed "s/'(-)'/'TMP_MINUS_IN_ROUND_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4         # change all '(-)' to 'TMP_MINUS_IN_ROUND_BRACKET' to change back later

sed "s/'\/\/'/'TMP_COMMENT'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                       # change all '//' to 'TMP_COMMENT' to change back later

sed "s/'<{'/'TMP_OPEN_TRIANGLE_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4           # change all '<{' to 'TMP_OPEN_TRIANGLE_BRACKET' to change back later
sed "s/'}>'/'TMP_CLOSE_TRIANGLE_BRACKET'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4          # change all '}>' to 'TMP_CLOSE_TRIANGLE_BRACKET' to change back later

sed "s/'@@'/'TMP_DOUBLE_AT_SYMBOL'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                # change all '@@' to 'TMP_DOUBLE_AT_SYMBOL' to change back later
sed "s/'@'/'TMP_SINGLE_AT_SYMBOL'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                 # change all '@' to 'TMP_SINGLE_AT_SYMBOL' to change back later

sed "s/(/( /g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                       # add a space after an open bracket to delete function calls later

sed "s/}?=>/}/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                     # change all }?=> to } to delete
sed "s/}?/}/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                       # change all }? to } to delete

sed "s/\]/}/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                       # change all ] to } to delete
sed "s/\[/{/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                       # change all [ to [ to delete

#Сleaning up
sed "s/grammar LsfLogics/grammar LsfJSLogics/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4      # change the grammar name
sed ':again;$!N;$!b again; :b; s/{[^{}]*}//g; t b' LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4   # remove all {} braces with nested
sed "s/@.*//g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                       # remove all lines starting with @
sed "s/scope//g" LsfJSLogics.g4 > tmp; mv tmp LsfJSLogics.g4                                      # remove all 'scope' words
sed "s/returns.*//g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                 # remove all 'returns.*' words

sed "s/\/\/.*//g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                    # remove comments
sed "s/\W\?\w\+ *=/ /g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                              # remove all function calls

sed "/^fragment RAW_STR_SPECIAL_CHAR[[:space:]]*:[[:space:]]*/s/NEXT_ID_LETTER/'a'..'z'|'A'..'Z'|'_'|'0'..'9'/" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4          # because links to rules(~(NEXT_ID_LETTER|SPACE)) don't work in antlr4. we have to inline them.
sed "/^fragment RAW_STR_SPECIAL_CHAR[[:space:]]*:[[:space:]]*/s/SPACE/' '|'\\\t'/" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                                       # because links to rules(~(NEXT_ID_LETTER|SPACE)) don't work in antlr4. we have to inline them.

sed "/^WS[[:space:]]*:[[:space:]]*/s/;/ -> channel(HIDDEN) ;/" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                      # turn off the space matcher
sed "/^COMMENTS[[:space:]]*:[[:space:]]*/s/;/ -> channel(HIDDEN) ;/" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                # turn off the comment matcher
sed "/^MULTILINE_COMMENTS[[:space:]]*:[[:space:]]*/s/;/ -> channel(HIDDEN) ;/" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4      # turn off the multiline_comment matcher

#Return back the required characters
sed "s/'TMP_OPEN_BRACKET'/'{'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                     # return back {
sed "s/'TMP_CLOSE_BRACKET'/'}'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                    # return back }

sed "s/'TMP_OPEN_SQUARE_BRACKET'/'\['/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4             # return back [
sed "s/'TMP_CLOSE_SQUARE_BRACKET'/'\]'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4            # return back ]

sed "s/'TMP_OPEN_ROUND_BRACKET'/'('/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4               # return back (
sed "s/'TMP_CLOSE_ROUND_BRACKET'/')'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4              # return back )

sed "s/'TMP_PLUS_IN_ROUND_BRACKET'/'(+)'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4          # return back (+)
sed "s/'TMP_MINUS_IN_ROUND_BRACKET'/'(-)'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4         # return back (-)

sed "s/'TMP_COMMENT'/'\/\/'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                       # return back '//'

sed "s/'TMP_OPEN_TRIANGLE_BRACKET'/'<{'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4           # return back <{
sed "s/'TMP_CLOSE_TRIANGLE_BRACKET'/'}>'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4          # return back }>

sed "s/'TMP_DOUBLE_AT_SYMBOL'/'@@'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                # return back @@
sed "s/'TMP_SINGLE_AT_SYMBOL'/'@'/g" LsfJSLogics.g4 > tmp ; mv tmp LsfJSLogics.g4                 # return back @

