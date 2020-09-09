package com.demo;

/**
 */
public class SingletonPattern {

    private SingletonPattern() {
    }

    public static SingletonPattern getInstance( ) {
        return   SingleEnum.INSTANCE.getSingletonPattern();
    }


    private enum SingleEnum {

        /**
         * 单例
         */
        INSTANCE;

        private SingletonPattern singletonPattern;

        SingleEnum() {
            this.singletonPattern = new SingletonPattern();
        }


        public SingletonPattern getSingletonPattern() {
            return singletonPattern;
        }
    }
}
