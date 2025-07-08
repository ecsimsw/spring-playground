import speech_recognition as sr

recognizer = sr.Recognizer()

with sr.Microphone() as source:
    print("음성을 입력하세요...")
    audio_data = recognizer.listen(source)
    print("인식 중...")

    try:
        text = recognizer.recognize_google(audio_data, language='ko-KR')
        print("인식 결과:", text)
    except sr.UnknownValueError:
        print("음성을 인식할 수 없습니다.")
    except sr.RequestError as e:
        print("구글 API 요청 실패; {0}".format(e))
