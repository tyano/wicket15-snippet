package jp.javelindev.wicket.component;

import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class TargetBox extends WebMarkupContainer {
    private static final long serialVersionUID = 1L;

    public TargetBox(String id) {
        super(id);
    }

    public void handleRemoveImage(RemoveRequest request) {
        setVisibilityAllowed(!request.isRemove());
    }
}
