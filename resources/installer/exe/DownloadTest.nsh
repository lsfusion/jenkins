


!macro DownloadFile SRC DEST
inetc::get ${SRC} ${DEST}
Messagebox MB_OK|MB_ICONINFORMATION ${DEST}
!macroend

!macro ReadUrlFromFile SRC DEST
FileOpen $4 ${SRC} r
FileRead $4 ${DEST} ; we read until the end of line (including carriage return and new line) and save it to $1
FileClose $4 ; and close the file
Messagebox MB_OK|MB_ICONINFORMATION ${DEST}
!macroend

Var SRC_LINK
Var DEST_LINK
Var SRC_JNLP
Var DEST_JNLP

Section "My Program"

;init urls
StrCpy $SRC_LINK "https://jenkins.lsfusion.org/download/exe/links/weblink-client-jnlp.lnk"
StrCpy $DEST_LINK "e:\work\1\test\client-jnlp.lnk"
StrCpy $DEST_JNLP "E://work/1/test/client.jnlp"

;download link
!insertmacro DownloadFile $SRC_LINK $DEST_LINK

;read url from downloaded link
!insertmacro ReadUrlFromFile $DEST_LINK $SRC_JNLP

;download client.jnlp from url
!insertmacro DownloadFile $SRC_JNLP $DEST_JNLP

SectionEnd
