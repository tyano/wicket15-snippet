package jp.javelindev.wicket;

import jp.javelindev.wicket.ajaxlist.AjaxListPage;
import jp.javelindev.wicket.bookmarkable.SamplePage;
import jp.javelindev.wicket.dispatcher.AnnotationEventDispatcher;
import jp.javelindev.wicket.form.FormPage;
import jp.javelindev.wicket.form.NextPage;
import jp.javelindev.wicket.movablelist.MovableListPage;
import jp.javelindev.wicket.page.CheckerBoardPage;
import jp.javelindev.wicket.page.Index;
import jp.javelindev.wicket.repeat.RepeatPage;
import jp.javelindev.wicket.resource.SimpleTextResource;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 *
 * @see jp.javelindev.wicket.Start#main(String[])
 */
public class WicketApplication extends WebApplication implements Rss {

    private HaseriRss rssSource;

    /**
     * @return HomePage
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return MovableListPage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getMarkupSettings().setStripWicketTags(true);

        MarkupFactory markupFactory = new MyMarkupFactory();
        getMarkupSettings().setMarkupFactory(markupFactory);

        getFrameworkSettings().add(new AnnotationEventDispatcher());
        mountPage("/index", Index.class);
        mountPage("/home/${name}/address/${address}", HomePage.class);
        mountPage("/checkerboard", CheckerBoardPage.class);
        mountPage("/ajaxlist", AjaxListPage.class);
        mountPage("/bookmarkable", SamplePage.class);
        mountPage("/form", FormPage.class);
        mountPage("/next", NextPage.class);
        mountPage("/repeat", RepeatPage.class);
        mountPage("/movable", MovableListPage.class);

        getSharedResources().add("simpletext", SimpleTextResource.create());
        ResourceReference reference = new SharedResourceReference("simpletext");

        mountResource("/simpletext", reference);
        rssSource = new HaseriRss();
    }

    @Override
    public HaseriRss getRssSource() {
        return rssSource;
    }

    @Override
    protected void onDestroy() {
        rssSource.stopCrawlingThread();
        rssSource = null;
        super.onDestroy();
    }

    public static WicketApplication get() {
        return (WicketApplication) Application.get();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new WicketSession(request);
    }
}
