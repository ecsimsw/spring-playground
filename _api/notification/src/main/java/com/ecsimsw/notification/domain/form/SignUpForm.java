package com.ecsimsw.notification.domain.form;

public class SignUpForm implements EmailForm {

    @Override
    public String subject() {
        return "가입 완료 안내";
    }

    public String body(String... args) {
        return String.format(FORMAT, args);
    }

    private static final String FORMAT = "<!DOCTYPE html>\n" +
            "<html lang=\"ko\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>가입 완료 메일</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            background-color: #f4f4f4;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        .container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 40px auto;\n" +
            "            background: #ffffff;\n" +
            "            padding: 20px;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        h1 {\n" +
            "            color: #333;\n" +
            "        }\n" +
            "        p {\n" +
            "            color: #666;\n" +
            "            font-size: 16px;\n" +
            "            line-height: 1.5;\n" +
            "        }\n" +
            "        .button {\n" +
            "            display: inline-block;\n" +
            "            background-color: #007BFF;\n" +
            "            color: #ffffff;\n" +
            "            padding: 12px 24px;\n" +
            "            margin-top: 20px;\n" +
            "            text-decoration: none;\n" +
            "            font-size: 16px;\n" +
            "            border-radius: 6px;\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .button:hover {\n" +
            "            background-color: #0056b3;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            margin-top: 20px;\n" +
            "            font-size: 12px;\n" +
            "            color: #999;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h1>가입을 축하드립니다! \uD83C\uDF89</h1>\n" +
            "        <p><strong> %s </strong>님, 환영합니다!<br> 계정이 성공적으로 생성되었습니다.</p>\n" +
            "        <p>관리자가 승인 후 로그인하실 수 있습니다.</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>\n";
}
