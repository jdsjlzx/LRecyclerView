/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lzx.demo.bean;

import android.support.annotation.NonNull;

import com.lzx.demo.type.TypeFactory;

import java.util.List;

public class ProductList implements Visitable {

    public List<Product> products;

    public ProductList(@NonNull List<Product> products) {this.products = products;}

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
