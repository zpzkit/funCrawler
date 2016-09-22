package crawler.dao;

import crawler.bean.SrcUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by watson zhang on 16/9/20.
 */
public class MyQueue implements BasicBox<SrcUrl>{
    Queue<SrcUrl> srcUrls;
    public MyQueue(){
        srcUrls = new PriorityQueue<>();
    }

    @Override
    public int getBoxSize(SrcUrl cache) {
        return srcUrls.size();
    }

    @Override
    public SrcUrl getOneFromBox(SrcUrl cache) {
        return srcUrls.poll();
    }

    @Override
    public List<SrcUrl> getListFromBox(SrcUrl cache, int start, int end) {
        return null;
    }

    @Override
    public List<SrcUrl> getListFromBox(SrcUrl cache, int num) {
        List<SrcUrl> srcUrlList = new ArrayList<>();
        for (int i = 0;i < num;i++){
            srcUrlList.add(srcUrls.poll());
        }
        return srcUrlList;
    }

    @Override
    public boolean addOne(SrcUrl cache) {
        boolean ret = srcUrls.add(cache);
        return ret;
    }

    @Override
    public int addList(List<SrcUrl> cache) {
        int i = 0;
        for (SrcUrl srcUrl : cache) {
            srcUrls.add(srcUrl);
            i++;
        }
        return i;
    }
}
