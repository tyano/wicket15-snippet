package jp.javelindev.wicket;

import jp.javelindev.wicket.ajaxlist.AjaxListPage;
import jp.javelindev.wicket.ajaxpage.AjaxPage;
import jp.javelindev.wicket.bookmarkable.SamplePage;
import jp.javelindev.wicket.decorator.DecoratorPage;
import jp.javelindev.wicket.dispatcher.AnnotationEventDispatcher;
import jp.javelindev.wicket.errors.Error404;
import jp.javelindev.wicket.form.FormPage;
import jp.javelindev.wicket.form.NextPage;
import jp.javelindev.wicket.form2.FormPage2;
import jp.javelindev.wicket.movablelist.MovableListPage;
import jp.javelindev.wicket.page.CheckerBoardPage;
import jp.javelindev.wicket.page.Index;
import jp.javelindev.wicket.repeat.RepeatPage;
import jp.javelindev.wicket.resource.SimpleTextResource;
import jp.javelindev.wicket.resourcedecoration.GroupingAndFilteringHeaderResponse;
import jp.javelindev.wicket.resourcedecoration.GroupingHeaderResponse;
import jp.javelindev.wicket.resourcedecoration.MergedResourcesResource;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.resource.filtering.JavaScriptFilteredIntoFooterHeaderResponse;

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
        return FormPage2.class;
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
        mountPage("/decorator", DecoratorPage.class);
        mountPage("/autolink", AutoLinkPage.class);
        mountPage("/ajax", AjaxPage.class);
        mountPage("/error404", Error404.class);

//        mount(new FixedUrlMountedMapper("/fixedform", FormPage.class));
//        mount(new FixedUrlMountedMapper("/fixedform2", FormPage2.class));
        mountPage("/form", FormPage.class);
        mountPage("/form2", FormPage2.class);

        getSharedResources().add("simpletext", SimpleTextResource.create());
        ResourceReference reference = new SharedResourceReference("simpletext");

        mountResource("/simpletext", reference);
        rssSource = new HaseriRss();

//        getSharedResources().add("merged-resources", new MergedResourcesResource());
//
//        setHeaderResponseDecorator(new IHeaderResponseDecorator() {
//            public IHeaderResponse decorate(IHeaderResponse response) {
//                // use grouping header response for the CSS resources, this way we can load several
//                // .css files in one http request. See HomePage#renderHead() header.css and
//                // footer.css
//                GroupingHeaderResponse groupingHeaderResponse = new GroupingHeaderResponse(response);
//
//                // use this header resource decorator to load all JavaScript resources in the page
//                // footer (after </body>)
//                JavaScriptFilteredIntoFooterHeaderResponse javaScriptFooterResponse = new JavaScriptFilteredIntoFooterHeaderResponse(
//                        response, "footerJS");
//
//                // finally use one that delegates to the two above
////                return new GroupingAndFilteringHeaderResponse(groupingHeaderResponse,
////                        javaScriptFooterResponse);
//
//                return new JavaScriptFilteredIntoFooterHeaderResponse(groupingHeaderResponse, "footerJS");
//            }
//        });
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
