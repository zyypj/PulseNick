package br.com.pulse.pulsenick.model;

public class PlayerNick {

    private final String realNick;
    private final String fakeNick;

    public PlayerNick(String realNick, String fakeNick) {
        this.realNick = realNick;
        this.fakeNick = fakeNick;
    }

    // Verifica se o jogador tem um nick fake
    public boolean hasFakeNick() {
        return fakeNick != null;
    }

    // Retorna o nick fake
    public String getFakeNick() {
        return fakeNick;
    }

    // Retorna o nick real
    public String getRealNick() {
        return realNick;
    }
}