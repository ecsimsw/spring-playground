import gradio as gr
import requests

API_KEY = "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"

def chat_gpt4_1(message, history):
    url = "https://openrouter.ai/api/v1/chat/completions"
    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json",
    }
    messages = []
    for user_msg, ai_msg in history:
        messages.append({"role": "user", "content": user_msg})
        messages.append({"role": "assistant", "content": ai_msg})
    messages.append({"role": "user", "content": message})  # 이번 질문 추가

    data = {
        "model": "openai/gpt-4.1",
        "messages": messages,
    }

    try:
        response = requests.post(url, headers=headers, json=data)
        output = response.json()['choices'][0]['message']['content']
    except Exception as e:
        output = f"오류 발생: {e}"
    return output

iface = gr.ChatInterface(
    fn=chat_gpt4_1,
    title="Chat JinPT"
)

iface.launch()
