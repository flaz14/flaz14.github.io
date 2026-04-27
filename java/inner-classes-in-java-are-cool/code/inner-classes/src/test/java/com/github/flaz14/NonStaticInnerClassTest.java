package com.github.flaz14;

import org.junit.Test;

public class NonStaticInnerClassTest {

    @Test
    public void test() throws Exception {
        final ChatBot bot = new ChatBot(new XmppClient());
        bot.startConversation();
        bot.broadcast();
    }
}

class ChatBot {
    private final XmppClient xmppClient;

    public ChatBot(XmppClient xmppClient) {
        this.xmppClient = xmppClient;
    }

    public void startConversation() {
        new Message("Hello!!!").sendImmediately();
    }

    public void broadcast() {
        for (int i = 0; i < 100; i++) {
            new Message("Spam...").sendAsynchronously();
        }
    }

    private class Message {
        private final String text;

        public Message(String text) {
            this.text = text;
        }

        public int sendImmediately() {
            // ...
            return 0;
        }

        public void sendAsynchronously() {
            // ...
        }
    }
}

class XmppClient {
    // ...
}
