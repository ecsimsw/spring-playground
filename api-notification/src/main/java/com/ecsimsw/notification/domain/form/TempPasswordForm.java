package com.ecsimsw.notification.domain.form;

public class TempPasswordForm implements EmailForm {

    public String subject() {
        return "임시 비밀번호 안내";
    }

    public String body(String... args) {
        return String.format(FORMAT, args);
    }

    private static final String FORMAT = "<!DOCTYPE html>\n" +
        "<html lang=\"ko\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "    <title>임시 비밀번호 안내</title>\n" +
        "    <style>\n" +
        "        body {\n" +
        "            font-family: Arial, sans-serif;\n" +
        "            background-color: #f4f4f4;\n" +
        "            text-align: center;\n" +
        "            padding: 50px;\n" +
        "        }\n" +
        "        .container {\n" +
        "            background-color: white;\n" +
        "            padding: 20px;\n" +
        "            border-radius: 8px;\n" +
        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
        "            display: inline-block;\n" +
        "        }\n" +
        "        h2 {\n" +
        "            color: #333;\n" +
        "        }\n" +
        "        .password-box {\n" +
        "            font-size: 20px;\n" +
        "            font-weight: bold;\n" +
        "            color: #d9534f;\n" +
        "            background-color: #f9f2f4;\n" +
        "            padding: 10px;\n" +
        "            border-radius: 5px;\n" +
        "            display: inline-block;\n" +
        "            margin: 15px 0;\n" +
        "        }\n" +
        "        .notice {\n" +
        "            font-size: 14px;\n" +
        "            color: #666;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "\n" +
        "    <div class=\"container\">\n" +
        "        <h2>임시 비밀번호 안내</h2>\n" +
        "        <p>아래 임시 비밀번호를 사용하여 로그인하세요.</p>\n" +
        "        <div class=\"password-box\"> %s </div>\n" +
        "        <p class=\"notice\">로그인 후 반드시 비밀번호를 변경해 주세요.</p>\n" +
        "    </div>\n" +
        "\n" +
        "</body>\n" +
        "</html>\n";
}