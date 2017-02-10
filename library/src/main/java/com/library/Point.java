/*
 * Copyright 2015 chenupt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.library;

import android.graphics.Rect;

/**
 * Created by chenupt@gmail.com on 2015/1/31.
 * Description : SpringView point
 */
public class Point
{

    private float x;
    private float y;
    private float radius;

    public Point()
    {
        rect.top = (int) (y - radius);
        rect.bottom = (int) (y + radius);
        rect.left = (int) (x - radius);
        rect.right = (int) (x + radius);
    }

    public Rect getRect()
    {
        return rect;
    }

    public void setRect(Rect rect)
    {
        this.rect = rect;
    }

    private Rect rect = new Rect();


    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        rect.left = (int) (x - radius);
        rect.right = (int) (x + radius);
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        rect.top = (int) (y - radius);
        rect.bottom = (int) (y + radius);
        this.y = y;
    }

    public float getRadius()
    {
        return radius;
    }

    public void setRadius(float radius)
    {
        rect.top = (int) (y - radius);
        rect.bottom = (int) (y + radius);
        rect.left = (int) (x - radius);
        rect.right = (int) (x + radius);
        this.radius = radius;
    }
}
