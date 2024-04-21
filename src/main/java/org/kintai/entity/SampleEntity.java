package org.kintai.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SampleEntity {
    // Objectify から利用できるように public にしているが、
    // public にせずにすむ方法があればそっちの方が良い

    // auto-generated id を使おうと思ったら Log になるっぽい
    public @Id Long id;
    public String name;

    // Objectify に Entity として登録するためには default constructor が必要
    public SampleEntity() {
    }

    public SampleEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
