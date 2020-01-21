package follow.twentyfourking.greendao_use_annotation.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Peopel {
    @Id
    private Long id;

    @NotNull
    private String name;

    @Generated(hash = 686424407)
    public Peopel(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 326257909)
    public Peopel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
