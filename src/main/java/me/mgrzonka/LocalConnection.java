package me.mgrzonka;

public class LocalConnection extends Connection{
    public LocalConnection(Node from, Node to) {
        super(from, to, 10, 10);
    }
}
