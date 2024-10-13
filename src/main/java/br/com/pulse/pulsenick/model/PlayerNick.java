package br.com.pulse.pulsenick.model;

public class PlayerNick {

    private final String nickReal;
    private final String nickFake;

    public PlayerNick(String nickReal, String nickFake) {
        this.nickReal = nickReal;
        this.nickFake = nickFake;
    }

    // Verifica se o jogador tem um nick fake
    public boolean hasNickFake() {
        return nickFake != null;
    }

    // Retorna o nick fake
    public String getNickFake() {
        return nickFake;
    }

    // Retorna o nick real
    public String getNickReal() {
        return nickReal;
    }
}