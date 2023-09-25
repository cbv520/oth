package mux.lib.auth.handshake;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
public abstract class HandshakeState {

    public static void main(String[] args) {
        Proposal p = new Proposal();
        HandshakeState.of(p).sendProposal();
        HandshakeState.of(p).receiveAckOrNak(true);

        p = new Proposal();
        HandshakeState.of(null).receiveProposal(p);
        HandshakeState.of(p).receiveAckOrNak(true);

    }

    public static HandshakeState of(Proposal p) {
        if (p == null || p.getState() == null  || State.valueOf(p.getState()) == State.INITIAL) {
            return new Initial(p);
        }
        if (State.valueOf(p.getState()) == State.PROPOSAL_SENT) {
            return new ProposalSent(p);
        }
        if (State.valueOf(p.getState()) == State.AWAITING_ACCEPT) {
            return new AwaitingAccept(p);
        }
        if (State.valueOf(p.getState()) == State.ACCEPTED) {
            return new Accepted(p);
        }
        if (State.valueOf(p.getState()) == State.ESTABLISHED) {
            return new Established(p);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public enum State {
        INITIAL,
        PROPOSAL_SENT,
        AWAITING_ACCEPT,
        ACCEPTED,
        ESTABLISHED
    }

    public static List<Predicate<Proposal>> acceptRules = List.of(p -> false);
    public static Predicate<Proposal> d = p -> acceptRules.stream().anyMatch(rule -> rule.test(p));

    protected Proposal proposal;

    public void sendProposal() {
        throw new IllegalStateException();
    }

    public void receiveProposal(Proposal proposal) {
        throw new IllegalStateException();
    }

    public void receiveAckOrNak(boolean ack) {
        throw new IllegalStateException();
    }

    public void acceptRejectOrModify(boolean accept, Proposal modified) {
        throw new IllegalStateException();
    }

    protected void setState(State state) {
        System.out.println("old state: " + proposal.getState() + " new state: " + state);
        proposal.setState(state.name());
        if (state == State.AWAITING_ACCEPT) {
            new AwaitingAccept(proposal).acceptRejectOrModify(d.test(proposal), null);
        }
    }

    protected void sendMessage() {
        System.out.println("i offer " + proposal);
    }

    protected void sendAck() {
        System.out.println("ack");
    }

    protected void sendNak() {
        System.out.println("nak");
    }

    protected void save() {
        System.out.println("save");
    }

    /********************************************************
     *
     ********************************************************/

    public static class Initial extends HandshakeState {

        public Initial(Proposal proposal) {
            super(proposal);
        }

        @Override
        public void sendProposal() {
            sendMessage();
            setState(State.PROPOSAL_SENT);
        }

        @Override
        public void receiveProposal(Proposal proposal) {
            this.proposal = proposal;
            setState(State.AWAITING_ACCEPT);
        }

        @Override
        public void receiveAckOrNak(boolean ack) { }
    }

    public static class ProposalSent extends HandshakeState {

        public ProposalSent(Proposal proposal) {
            super(proposal);
        }

        @Override
        public void receiveAckOrNak( boolean ack) {
            if (ack) {
                setState(State.ESTABLISHED);
                save();
                sendAck();
            } else {
                setState(State.INITIAL);
            }
        }

        @Override
        public void receiveProposal(Proposal proposal) {
            setState(State.AWAITING_ACCEPT);
        }

    }

    public static class Established extends HandshakeState {

        public Established(Proposal proposal) {
            super(proposal);
        }

        @Override
        public void receiveAckOrNak(boolean ack) { }

        @Override
        public void acceptRejectOrModify(boolean accept, Proposal modified) { }

        @Override
        public void receiveProposal(Proposal proposal) {
            setState(State.AWAITING_ACCEPT);
        }
    }

    public static class AwaitingAccept extends HandshakeState {

        public AwaitingAccept(Proposal proposal) {
            super(proposal);
        }

        @Override
        public void acceptRejectOrModify( boolean accept, Proposal modified) {
            if (modified != null) {
                setState(State.PROPOSAL_SENT);
                sendMessage();
                return;
            }
            if (accept) {
                setState(State.ACCEPTED);
                sendAck();
            } else {
                setState(State.INITIAL);
                sendNak();
            }
        }

    }

    public static class Accepted extends HandshakeState {

        public Accepted(Proposal proposal) {
            super(proposal);
        }

        @Override
        public void receiveAckOrNak( boolean ack) {
            if (ack) {
                setState(State.ESTABLISHED);
                save();
            } else {
                throw new IllegalStateException();
            }
        }

    }
}
