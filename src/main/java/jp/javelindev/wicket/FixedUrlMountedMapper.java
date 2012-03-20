/*
 * Copyright 2012 Tsutomu YANO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.javelindev.wicket;

import org.apache.wicket.Session;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.mapper.MountedMapper;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.ClassProvider;

/**
 *
 * @author Tsutomu YANO
 */
public class FixedUrlMountedMapper extends MountedMapper {

    public FixedUrlMountedMapper(String mountPath, ClassProvider<? extends IRequestablePage> pageClassProvider, IPageParametersEncoder pageParametersEncoder) {
        super(mountPath, pageClassProvider, pageParametersEncoder);
    }

    public FixedUrlMountedMapper(String mountPath, Class<? extends IRequestablePage> pageClass, IPageParametersEncoder pageParametersEncoder) {
        super(mountPath, pageClass, pageParametersEncoder);
    }

    public FixedUrlMountedMapper(String mountPath, ClassProvider<? extends IRequestablePage> pageClassProvider) {
        super(mountPath, pageClassProvider);
    }

    public FixedUrlMountedMapper(String mountPath, Class<? extends IRequestablePage> pageClass) {
        super(mountPath, pageClass);
    }

    /**
     * {@inheritDoc }
     * <p>
     * スーパークラスの実装は、ページインスタンスがステートレスの場合のみ、URLにページIDを付与しない。<br>
     * このサブクラスは、上記に加え、ページインスタンスのページIDがページIDストアに保存されているページIDと
     * 一致する時も、URLにページIDを埋め込まない。
     */
    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        //requestHandlerがRenderPageRequestHandlerの時だけ特別な処理を実施する。
        //それ以外はスーパークラスに任せる
        if (requestHandler instanceof RenderPageRequestHandler) {
            // possibly hybrid URL - bookmarkable URL with page instance information
            // but only allowed if the page was created by bookmarkable URL

            RenderPageRequestHandler handler = (RenderPageRequestHandler) requestHandler;

            if (!checkPageClass(handler.getPageClass())) {
                return null;
            }

            if (handler.getPageProvider().isNewPageInstance()) {
                // no existing page instance available, don't bother creating new page instance
                PageInfo info = new PageInfo();
                UrlInfo urlInfo = new UrlInfo(new PageComponentInfo(info, null),
                        handler.getPageClass(), handler.getPageParameters());

                return buildUrl(urlInfo);
            }

            IRequestablePage page = handler.getPage();

            if (checkPageInstance(page)
                    && (!pageMustHaveBeenCreatedBookmarkable() || page.wasCreatedBookmarkable())) {
                PageInfo info = null;

                //ページがステートレスのときに加え、リクエストハンドラの持つページIDがセッションに保存されている初期ページIDと
                //一致する場合もURLにページIDを付与しない
                if (!page.isPageStateless() && !isStoredPage(handler.getPageClass(), page.getPageId())) {
                    info = new PageInfo(page.getPageId());
                }
                PageComponentInfo pageComponentInfo = info != null ? new PageComponentInfo(info,
                        null) : null;

                UrlInfo urlInfo = new UrlInfo(pageComponentInfo, page.getClass(),
                        handler.getPageParameters());
                return buildUrl(urlInfo);
            } else {
                return null;
            }
        } else {
            return super.mapHandler(requestHandler);
        }
    }

    protected final boolean isStoredPage(Class<? extends IRequestablePage> pageClass, Integer pageId) {
        Session session = Session.get();
        if (session != null && session instanceof IInitialPageIdStore) {
            IInitialPageIdStore store = (IInitialPageIdStore) session;
            Integer storedPageId = store.getInitialId(pageClass);
            return storedPageId != null && storedPageId.equals(pageId);
        }
        return false;
    }

    protected final Integer getStoredPageId(Class<? extends IRequestablePage> pageClass) {
        Session session = Session.get();
        if (session != null && session instanceof IInitialPageIdStore) {
            IInitialPageIdStore store = (IInitialPageIdStore) session;
            return store.getInitialId(pageClass);
        }
        return null;
    }

    /**
     * {@inheritDoc }
     * <p>
     * requestにpageIdが埋め込まれていない場合でも、表示しようとしているページインスタンスの生成時ページIDがセッションに
     * 保存されている場合は、保存されているページIDを使用します。
     */
    @Override
    public IRequestHandler mapRequest(Request request) {
        UrlInfo urlInfo = parseRequest(request);

        // check if the URL is long enough and starts with the proper segments
        if (urlInfo != null) {
            PageComponentInfo info = urlInfo.getPageComponentInfo();
            Class<? extends IRequestablePage> pageClass = urlInfo.getPageClass();
            PageParameters pageParameters = urlInfo.getPageParameters();

            Integer storedPageId = getStoredPageId(pageClass);

            //リクエストにページIDが埋め込まれていない場合はブックマークページとして扱う。
            //さらに、ページIDストアにページIDが保存されていないことも確認し、
            //ページIDが保存済みの場合も、ブックマークページとしては扱わない。
            //processBookmarkableメソッドもオーバーライドされており、独自のPageProviderを使用している。
            //独自のPageProviderは、ページインスタンス生成時に、生成したページのページIDをページIDストアに保存する。
            if ((info == null || info.getPageInfo().getPageId() == null) && storedPageId == null) {
                // if there are is no page instance information (only page map name - optionally)
                // then this is a simple bookmarkable URL
                return processBookmarkable(pageClass, pageParameters);
            } else if (((info != null && info.getPageInfo() != null && info.getPageInfo().getPageId() != null) || storedPageId != null) && 
                       (info == null || info.getComponentInfo() == null)) {
                // if there is page instance information in the URL but no component and listener
                // interface then this is a hybrid URL - we need to try to reuse existing page
                // instance

                // URLにページIDが埋め込まれている場合はそちらを優先して使用する。
                // URLにページIDがなく、storedPageID != null の場合、storedPageIdをページIDとして使用する。
                PageInfo pageInfo = info == null ? null : info.getPageInfo();
                if ((pageInfo == null || pageInfo.getPageId() == null) && storedPageId != null) {
                    pageInfo = new PageInfo(storedPageId);
                }

                return processHybrid(pageInfo, pageClass, pageParameters, null);
            } else if (info != null && info.getComponentInfo() != null) {
                // with both page instance and component+listener this is a listener interface URL
                return processListener(info, pageClass, pageParameters);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc }
     * <p>
     * この実装では、PageProviderクラスの代わりに、{@link InitialPageStorePageProvider}クラスを使用する。
     * {@link InitialPageStorePageProvider} は、新しいページインスタンスを生成したときに、ページIDストアに
     * 生成時のページIDを記録する。
     *
     */
    @Override
    protected IRequestHandler processBookmarkable(Class<? extends IRequestablePage> pageClass,
            PageParameters pageParameters) {
        PageProvider provider = new InitialPageStorePageProvider(pageClass, pageParameters);
        provider.setPageSource(getContext());
        return new RenderPageRequestHandler(provider);
    }
}
