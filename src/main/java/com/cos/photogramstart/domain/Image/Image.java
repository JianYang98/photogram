package com.cos.photogramstart.domain.Image;

import com.cos.photogramstart.domain.Likes.Likes;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder // 패턴으로 데이터를 담을 수 있게 해주는 어노테이션
@AllArgsConstructor // 모든 생성자 자동으로 만들어주는 너도 테이션
@NoArgsConstructor // 빈 생성자 자동으로 만들어주는 어노테이션
@Data   // 자동으로 getter,setter,toString 만들어주는 어노테이션
@Entity // 디비에 테이블 생성
public class Image { // N , 1 // N대 1의 관계임
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    private  String caption ; // 글
    private  String postImageUrl ;
    // 1)사진을 전송받아서 그 사진을 서버 특정 폴더에 저장 2) DB 그 저장된 경로를 insert 할거임
    @JsonIgnoreProperties({"images"}) // user 들고올때 images는 들고 오지 마라!
    @ManyToOne(fetch = FetchType.EAGER) //  select 하면서 조인하고 user들고옴
    @JoinColumn(name = "userId")
    private User user ; // 1 , 1

    // 이미지 좋아요
    @JsonIgnoreProperties({"image"}) // Likes 내부에 있는 image 참조 안하게!(무한참조 방지)
    @OneToMany(mappedBy = "image") //likes의 image변수 이름
    private List<Likes> likes ; // 하나의 이미지에 여러개의 좋아요 가능 이니 원두맨

    @Transient // DB에 컬럼이 만들어 지지 않는다.
    private boolean likesState ;

    @Transient // 조아요 갯수 DB 킬람 X
    private int likeCount;

    // 댓글 Lazy로딩
    @OrderBy("id DESC")
    @JsonIgnoreProperties({"image"}) // comment내부에서 image가져오지맘!
    @OneToMany(mappedBy = "image") //comment안에 이미지 //연관관계의 주인이 아닙니다. //FK 변수를 mappedBy에 적어둠
    private List<Comment> comments ;  // 하나에 이미지에 여러개의 코맨트 쌉가능 


    private LocalDateTime createDate ; // 만든 날짜


    @PrePersist // 디비에 자동으로 insert되기 직전 실행
    public void createDate(){
        this.createDate = LocalDateTime.now() ;
    }


}
