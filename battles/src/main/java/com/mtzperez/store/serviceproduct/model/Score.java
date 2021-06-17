package com.mtzperez.store.serviceproduct.model;


import lombok.Builder;
import lombok.Data;
import java.util.Date;


@Data @Builder
public class Score {

        private Long id;
        private Double total;
        private Boolean extra;
        private String status;
        private Date createdAt;
        private Date updatedAt;
        private Long styleId;
}
