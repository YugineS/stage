package com.yug.core.game.model;

import com.yug.core.game.GameWorld;

/**
 * Created by yugine on 25.2.15.
 */
public class Teleport extends Tile
{
    /**teleport unique id*/
    private int id;
    /**unique id of the target teleport*/
    private int targetId;
    private Teleport targetTeleport;
    private State state = State.WAITING;

    public void update(final float deltaT)
    {
        this.state.getStateHandler().update(this, deltaT);
    }

    public Teleport getTargetTeleport()
    {
        return targetTeleport;
    }

    public void setTargetTeleport(final Teleport targetTeleport)
    {
        this.targetTeleport = targetTeleport;
    }

    public State getState()
    {
        return state;
    }

    public void setState(final State state)
    {
        this.state = state;
        this.state.getStateHandler().enterState(this);
    }

    public int getId()
    {
        return id;
    }

    public void setId(final int id)
    {
        this.id = id;
    }

    public int getTargetId()
    {
        return targetId;
    }

    public void setTargetId(final int targetId)
    {
        this.targetId = targetId;
    }

    public enum State
    {
        WAITING(new WaitingStateHandler()),
        SENDING(new SendingStateHandler()),
        RECEIVING(new ReceivingStateHandler()),
        RECEIVED(new ReceivedStateHandler());
        private StateHandler stateHandler;

        private State(final StateHandler stateHandler)
        {
            this.stateHandler = stateHandler;
        }

        public StateHandler getStateHandler()
        {
            return this.stateHandler;
        }
    }

    private static interface StateHandler
    {
        void enterState(Teleport teleport);

        void update(Teleport teleport, float deltaT);
    }

    private static class WaitingStateHandler implements StateHandler
    {
        @Override
        public void enterState(final Teleport teleport)
        {

        }

        @Override
        public void update(final Teleport teleport, float deltaT)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            if (player.getX() == teleport.getX() && player.getY() == teleport.getY())
            {
                teleport.setState(State.SENDING);
            }
        }
    }

    private static class SendingStateHandler implements StateHandler
    {
        @Override
        public void enterState(final Teleport teleport)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            player.setState(Player.State.TELEPORTING);
            teleport.getTargetTeleport().setState(State.RECEIVING);
        }

        @Override
        public void update(final Teleport teleport, final float deltaT)
        {
            //start sending animation
            //onSending animation ends
            teleport.setState(State.WAITING);
        }
    }

    private static class ReceivingStateHandler implements StateHandler
    {
        @Override
        public void enterState(final Teleport teleport)
        {

        }

        @Override
        public void update(final Teleport teleport, final float deltaT)
        {
            //start receiving animation
            //onReciving animation ends
            GameWorld.getInstance().getPlayer().teleportTo(teleport.getX(), teleport.getY());
            teleport.setState(State.RECEIVED);
        }
    }

    private static class ReceivedStateHandler implements StateHandler
    {
        @Override
        public void enterState(final Teleport teleport)
        {
            GameWorld.getInstance().getPlayer().setState(Player.State.STANDING);
        }

        @Override
        public void update(final Teleport teleport, final float deltaT)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            if (player.getX() != teleport.getX() || player.getY() != teleport.getY())
            {
                teleport.setState(State.WAITING);
            }
        }
    }

}
