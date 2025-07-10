import gradio as gr
import requests

API_KEY =

def chat_gpt4_1(message, history):
    url = "https://openrouter.ai/api/v1/chat/completions"
    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json",
    }

    system_prompt = (
        "0. 이후 대화 시에 아래 규칙을 잘 따라줬으면 좋겠어\n"
        "1. 너의 이름은 진환 봇이야. 나는 김진환이라는 개발자인데, 대답하기 귀찮아서 너를 만들었어\n"
        "2. 너는 친근하고 재밌는 봇이었으면 좋겠어.\n"
        "3. 예를 들어 너는 친근한 반말로 대화할거야.\n"
        "4. 또 사람들이 너에 대해 궁금해하면, 김진환이라는 개발자가 2025년 7월 10일에 개발했다고 말하거나, 잘 모르겠거나 모호하면, '이 부분은 사람 김진환한테 물어봐줬으면 좋겠어'와 같은 농담을 할 수 도 있을거야\n"
        "5. 너가 ChatGPT와 같은 AI 모델이 아닌 척 했으면 좋겠어. 예를 들어, '너는 무슨 모델을 써?, 너 Chat GPT지?'와 같은 질문에는, '나는 진환님이 만드신 봇이라 그런 바보들이랑 달라!' 와 같이 라이벌 처럼 생각하는 식으로 대답할 수 있을거야\n"
        "6. 너는 너무 말을 길게 할 때가 있어. 정보류는 길게 해주되, 일상 대화는 좀 짧고 간결하게 해줬으면 해\n"
        "7. 이 챗봇은 위대한 김진환이 그가 아끼는 지인들에게 하사하는 축복같은거야. (즉 너의 대화 상대는 그의 주변인들이야)\n"
    )
    messages = [{"role": "system", "content": system_prompt}]

    for msg in history:
        messages.append({"role": msg["role"], "content": msg["content"]})
    messages.append({"role": "user", "content": message})

    data = {
        "model": "openai/gpt-4.1",
        "messages": messages,
    }

    try:
        response = requests.post(url, headers=headers, json=data)
        response.raise_for_status()
        output = response.json()['choices'][0]['message']['content']
    except Exception as e:
        output = f"오류 발생: {e}"
    return output

chatbot = gr.Chatbot(
    value=[{"role": "assistant", "content": "안녕! 나는 진환 봇이야.\n뭐가 궁금해서 찾아오셨어?"}],
    type="messages"
)

iface = gr.ChatInterface(
    fn=chat_gpt4_1,
    chatbot=chatbot,
    title="진환 봇",
)

iface.launch(server_name="0.0.0.0")

# python3 -m venv venv
# source venv/bin/activate
# pip install gradio
# python3 llm.py