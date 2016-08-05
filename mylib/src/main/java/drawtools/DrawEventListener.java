package drawtools;

import java.util.EventListener;

/**
 * 
 * @author ropp gispace@yeah.net
 *
 *	���廭ͼ�¼������ӿ�
 */
public interface DrawEventListener extends EventListener {

	void handleDrawEvent(DrawEvent event);
}
