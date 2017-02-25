/*
 * The Seventh
 * see license.txt 
 */
package seventh.client.gfx.effects;

import seventh.client.gfx.Renderable;

/**
 * A visual effect
 * 
 * @author Tony
 *
 */
public interface Effect extends Renderable {

    /**
     * @return true if this effect is done
     */
    public boolean isDone();
    public void destroy();
}
