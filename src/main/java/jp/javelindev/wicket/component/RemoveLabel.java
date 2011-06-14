package jp.javelindev.wicket.component;

import jp.javelindev.wicket.dispatcher.EventHandler;
import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class RemoveLabel extends Label {
    private static final long serialVersionUID = 1L;

    public RemoveLabel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setDefaultModel(Model.of(getString("removeLabel")));
    }

    
    @EventHandler(payload=RemoveRequest.class)
    public void handleRemove(RemoveRequest request) {
        String newLabel = request.isRemove() ? getString("displayLabel") : getString("removeLabel");
        setDefaultModelObject(newLabel);
    }
}
