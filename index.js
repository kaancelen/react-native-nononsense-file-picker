import React, { Component } from 'react';
import {NativeModules} from 'react-native';

const RNNoNonsenseFilePickerManager = NativeModules.RNNoNonsenseFilePickerModule;

export default class RNNFP {

    static pick(args){
        if(RNNoNonsenseFilePickerManager){
            return RNNoNonsenseFilePickerManager.pick(args);
        }

        throw new Exception('RNNoNonsenseFilePickerManager#UnsupportedOperation');
    }

}