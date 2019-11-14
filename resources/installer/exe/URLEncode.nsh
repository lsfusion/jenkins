!include LogicLib.nsh
!include CharToASCII.nsh
 
Function URLEncode
    System::Store S ; Save registers
    Pop $0 ;param
    StrCpy $1 -1 ;init counter
    StrCpy $9 "" ; init buffer
 
NextChar:
    IntOp $1 $1 + 1
    StrCpy $2 $0 1 $1 ;get char
 
    ${If} $2 == ""
        Goto Done
    ${EndIf}
 
    ${CharToASCII} $3 $2 ; get charcode
 
    ${If} $3 == 32
        StrCpy $9 "$9+"
        Goto NextChar
    ${EndIf}
 
    ${If} $3 == 95
    ${OrIf} $3 == 45
        StrCpy $9 $9$2
        Goto NextChar
    ${EndIf}
 
    ${If} $3 >= 33
    ${AndIf} $3 <= 47
        IntFmt $4 "%X" $3
        StrCpy $9 $9%$4
        Goto NextChar
    ${EndIf}
 
    ${If} $3 >= 58
    ${AndIf} $3 <= 64
        IntFmt $4 "%X" $3
        StrCpy $9 $9%$4
        Goto NextChar
    ${EndIf}
 
    ${If} $3 >= 91
    ${AndIf} $3 <= 96
        IntFmt $4 "%X" $3
        StrCpy $9 $9%$4
        Goto NextChar
    ${EndIf}
 
    ${If} $3 >= 123
        IntFmt $4 "%X" $3
        StrCpy $9 $9%$4
        Goto NextChar
    ${EndIf}
 
 
    StrCpy $9 $9$2
    Goto NextChar
 
Done:
    Push $9
    System::Store L ; Restore registers
FunctionEnd