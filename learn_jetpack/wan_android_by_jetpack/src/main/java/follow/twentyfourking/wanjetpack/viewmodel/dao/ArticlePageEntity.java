package follow.twentyfourking.wanjetpack.viewmodel.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.ArticleTagBean;

@Entity(tableName = "article_list")
@TypeConverters(value = {ArticleConvert.class})
public class ArticlePageEntity {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private long romId;

    @ColumnInfo
    private String apkLink;
    @ColumnInfo
    private int audit;
    @ColumnInfo
    private String author;
    @ColumnInfo
    private int chapterId;
    @ColumnInfo
    private String chapterName;
    @ColumnInfo
    private boolean collect;
    @ColumnInfo
    private int courseId;
    @ColumnInfo
    private String desc;
    @ColumnInfo
    private String envelopePic;
    @ColumnInfo
    private boolean fresh;
    @ColumnInfo
    private long id;
    @ColumnInfo
    private String link;
    @ColumnInfo
    private String niceDate;
    @ColumnInfo
    private String niceShareDate;
    @ColumnInfo
    private String origin;
    @ColumnInfo
    private String prefix;
    @ColumnInfo
    private String projectLink;
    @ColumnInfo
    private long publishTime;
    @ColumnInfo
    private int selfVisible;
    @ColumnInfo
    private long shareDate;
    @ColumnInfo
    private String shareUser;
    @ColumnInfo
    private int superChapterId;
    @ColumnInfo
    private String superChapterName;
    //    @Embedded(prefix = "article_tag")
    @ColumnInfo
    private List<ArticleTagBean> tags;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private int type;
    @ColumnInfo
    private int userId;
    @ColumnInfo
    private int visible;
    @ColumnInfo
    private int zan;

    public ArticlePageEntity(long romId, String apkLink, int audit, String author, int chapterId, String chapterName, boolean collect, int courseId, String desc, String envelopePic, boolean fresh, long id, String link, String niceDate, String niceShareDate, String origin, String prefix, String projectLink, long publishTime, int selfVisible, long shareDate, String shareUser, int superChapterId, String superChapterName, List<ArticleTagBean> tags, String title, int type, int userId, int visible, int zan) {
        this.romId = romId;
        this.apkLink = apkLink;
        this.audit = audit;
        this.author = author;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.collect = collect;
        this.courseId = courseId;
        this.desc = desc;
        this.envelopePic = envelopePic;
        this.fresh = fresh;
        this.id = id;
        this.link = link;
        this.niceDate = niceDate;
        this.niceShareDate = niceShareDate;
        this.origin = origin;
        this.prefix = prefix;
        this.projectLink = projectLink;
        this.publishTime = publishTime;
        this.selfVisible = selfVisible;
        this.shareDate = shareDate;
        this.shareUser = shareUser;
        this.superChapterId = superChapterId;
        this.superChapterName = superChapterName;
        this.tags = tags;
        this.title = title;
        this.type = type;
        this.userId = userId;
        this.visible = visible;
        this.zan = zan;
    }

    public List<ArticleTagBean> getTags() {
        return tags;
    }

    public void setTags(List<ArticleTagBean> tags) {
        this.tags = tags;
    }

    public long getRomId() {
        return romId;
    }

    public String getApkLink() {
        return apkLink;
    }

    public int getAudit() {
        return audit;
    }

    public String getAuthor() {
        return author;
    }

    public int getChapterId() {
        return chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public boolean isCollect() {
        return collect;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getDesc() {
        return desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public boolean isFresh() {
        return fresh;
    }

    public long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public String getNiceShareDate() {
        return niceShareDate;
    }

    public String getOrigin() {
        return origin;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public int getSelfVisible() {
        return selfVisible;
    }

    public long getShareDate() {
        return shareDate;
    }

    public String getShareUser() {
        return shareUser;
    }

    public int getSuperChapterId() {
        return superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

//    public List<ArticleTagBean> getTags() {
//        return tags;
//    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public int getVisible() {
        return visible;
    }

    public int getZan() {
        return zan;
    }

    public void setRomId(long romId) {
        this.romId = romId;
    }

    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public void setNiceShareDate(String niceShareDate) {
        this.niceShareDate = niceShareDate;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public void setSelfVisible(int selfVisible) {
        this.selfVisible = selfVisible;
    }

    public void setShareDate(long shareDate) {
        this.shareDate = shareDate;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public void setSuperChapterId(int superChapterId) {
        this.superChapterId = superChapterId;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

//    public void setTags(List<ArticleTagBean> tags) {
//        this.tags = tags;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }
}
