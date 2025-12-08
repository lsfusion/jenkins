Var cbMyCompanyLanguage
Var myCompanyLanguage

Function myCompanyConfigPagePre
    ${IfNot} ${SectionIsSelected} ${SecServer}
        Abort
    ${endIf}

    !insertmacro MUI_HEADER_TEXT $(strMyCompanyOptions) ""

    nsDialogs::Create /NOUNLOAD 1018

    StrCpy $0 "0"
    
    ${LS_CreateControl} ComboBox "$(strChooseLanguage)" $myCompanyLanguage $cbMyCompanyLanguage
    ${NSD_CB_AddString} $cbMyCompanyLanguage $(strSystemLanguage)
    ${NSD_CB_AddString} $cbMyCompanyLanguage $(strEnglish)
    ${NSD_CB_AddString} $cbMyCompanyLanguage $(strRussian)
    ${NSD_CB_SelectString} $cbMyCompanyLanguage $(strSystemLanguage)

    nsDialogs::Show
FunctionEnd

Function myCompanyConfigPageLeave
    ${NSD_GetText} $cbMyCompanyLanguage $myCompanyLanguage
    ${if} $myCompanyLanguage == $(strRussian)
        StrCpy $serverLanguage "ru"
        StrCpy $serverCountry "RU"
        StrCpy $topModule "MyCompanyRu"
    ${endIf}
    ${if} $myCompanyLanguage == $(strEnglish)
        StrCpy $serverLanguage "en"
        StrCpy $serverCountry "US"
    ${endIf}
FunctionEnd
