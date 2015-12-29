package imageprocessing;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

public class PixelFactoryJava {
	
	public static Bitmap mark(Bitmap src, String watermark, Point location, Color color, int alpha, int size, boolean underline) {
	    int w = src.getWidth();
	    int h = src.getHeight();
	    Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
	 
	    Canvas canvas = new Canvas(result);
	    canvas.drawBitmap(src, 0, 0, null);
	 
	    Paint paint = new Paint();
	    //.setColor(color);
	    paint.setAlpha(alpha);
	    paint.setTextSize(size);
	    paint.setAntiAlias(true);
	    paint.setUnderlineText(underline);
	    canvas.drawText(watermark, location.x, location.y, paint);
	 
	    return result;
	}
	
	
	public static Bitmap doHighlightImage(Bitmap src) {
	    // create new bitmap, which will be painted and becomes result image
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Bitmap.Config.ARGB_8888);
	    // setup canvas for painting
	    Canvas canvas = new Canvas(bmOut);
	    // setup default color
	    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
	 
	    // create a blur paint for capturing alpha
	    Paint ptBlur = new Paint();
	    ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
	    int[] offsetXY = new int[2];
	    // capture alpha into a bitmap
	    Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
	    // create a color paint
	    Paint ptAlphaColor = new Paint();
	    ptAlphaColor.setColor(0xFFFFFFFF);
	    // paint color for captured alpha region (bitmap)
	    canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
	    // free memory
	    bmAlpha.recycle();
	 
	    // paint the image source
	    canvas.drawBitmap(src, 0, 0, null);
	 
	    // return out final image
	    return bmOut;
	}
	
	
	public static Bitmap roundCorner(Bitmap src, float round) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create bitmap output
	    Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    // set canvas for painting
	    Canvas canvas = new Canvas(result);
	    canvas.drawARGB(0, 0, 0, 0);
	 
	    // config paint
	    final Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(Color.BLACK);
	 
	    // config rectangle for embedding
	    final Rect rect = new Rect(0, 0, width, height);
	    final RectF rectF = new RectF(rect);
	 
	    // draw rect to canvas
	    canvas.drawRoundRect(rectF, round, round, paint);
	 
	    // create Xfer mode
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    // draw source image to canvas
	    canvas.drawBitmap(src, rect, rect, paint);
	 
	    // return final image
	    return result;
	}
	
	
	public static Bitmap applyReflection(Bitmap originalImage) {
	    // gap space between original and reflected
	    final int reflectionGap = 4;
	    // get image size
	    int width = originalImage.getWidth();
	    int height = originalImage.getHeight();          
	 
	    // this will not scale but will flip on the Y axis
	    Matrix matrix = new Matrix();
	    matrix.preScale(1, -1);
	       
	    // create a Bitmap with the flip matrix applied to it.
	    // we only want the bottom half of the image
	    Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);          
	           
	    // create a new bitmap with same width but taller to fit reflection
	    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);
	     
	    // create a new Canvas with the bitmap that's big enough for
	    // the image plus gap plus reflection
	    Canvas canvas = new Canvas(bitmapWithReflection);
	    // draw in the original image
	    canvas.drawBitmap(originalImage, 0, 0, null);
	    // draw in the gap
	    Paint defaultPaint = new Paint();
	    canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
	    // draw in the reflection
	    canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
	      
	    // create a shader that is a linear gradient that covers the reflection
	    Paint paint = new Paint();
	    LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
	            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
	            TileMode.CLAMP);
	    // set the paint to use this shader (linear gradient)
	    paint.setShader(shader);
	    // set the Transfer mode to be porter duff and destination in
	    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	    // draw a rectangle using the paint with our linear gradient
	    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
	             
	    return bitmapWithReflection;
	}
	
	public static Bitmap emboss(Bitmap src) {
	    double[][] EmbossConfig = new double[][] {
	        { -1 ,  0, -1 },
	        {  0 ,  4,  0 },
	        { -1 ,  0, -1 }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(EmbossConfig);
	    convMatrix.Factor = 1;
	    convMatrix.Offset = 127;
	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	
	
	public static final int COLOR_MIN = 0x00;
	public static final int COLOR_MAX = 0xFF;
	 
	public static Bitmap applyFleaEffect(Bitmap source) {
	    // get image size
	    int width = source.getWidth();
	    int height = source.getHeight();
	    int[] pixels = new int[width * height];
	    // get pixel array from source
	    source.getPixels(pixels, 0, width, 0, 0, width, height);
	    // a random object
	    Random random = new Random();
	 
	    int index = 0;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	            // get current index in 2D-matrix
	            index = y * width + x;
	            // get random color
	            int randColor = Color.rgb(random.nextInt(COLOR_MAX),
	                    random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
	            // OR
	            pixels[index] |= randColor;
	        }
	    }
	    // output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bmOut;
	}
	
	
	public static Bitmap applySnowEffect(Bitmap source) {
	    // get image size
	    int width = source.getWidth();
	    int height = source.getHeight();
	    int[] pixels = new int[width * height];
	    // get pixel array from source
	    source.getPixels(pixels, 0, width, 0, 0, width, height);
	    // random object
	    Random random = new Random();
	     
	    int R, G, B, index = 0, thresHold = 50;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	            // get current index in 2D-matrix
	            index = y * width + x;              
	            // get color
	            R = Color.red(pixels[index]);
	            G = Color.green(pixels[index]);
	            B = Color.blue(pixels[index]);
	            // generate threshold
	            thresHold = random.nextInt(COLOR_MAX);
	            if(R > thresHold && G > thresHold && B > thresHold) {
	                pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
	            }                           
	        }
	    }
	    // output bitmap                
	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bmOut;
	}
	
	
	public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // constant grayscale
	    final double GS_RED = 0.3;
	    final double GS_GREEN = 0.59;
	    final double GS_BLUE = 0.11;
	    // color information
	    int A, R, G, B;
	    int pixel;
	 
	    // scan through all pixels
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            // get color on each channel
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // apply grayscale sample
	            B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	 
	            // apply intensity level for sepid-toning on each channel
	            R += (depth * red);
	            if(R > 255) { R = 255; }
	 
	            G += (depth * green);
	            if(G > 255) { G = 255; }
	 
	            B += (depth * blue);
	            if(B > 255) { B = 255; }
	 
	            // set new pixel color to output image
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	
	public static Bitmap doColorFilter(Bitmap src, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
 
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
 
        // return final image
        return bmOut;
    }
	
	
	public static Bitmap createContrast(Bitmap src, double value) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;
	    // get contrast value
	    double contrast = Math.pow((100 + value) / 100, 2);
	 
	    // scan through all pixels
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            // apply filter contrast for every channel R, G, B
	            R = Color.red(pixel);
	            R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(R < 0) { R = 0; }
	            else if(R > 255) { R = 255; }
	 
	            G = Color.red(pixel);
	            G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(G < 0) { G = 0; }
	            else if(G > 255) { G = 255; }
	 
	            B = Color.red(pixel);
	            B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(B < 0) { B = 0; }
	            else if(B > 255) { B = 255; }
	 
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	
	public static Bitmap doInvert(Bitmap src) {
	    // create new bitmap with the same settings as source bitmap
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	    // color info
	    int A, R, G, B;
	    int pixelColor;
	    // image size
	    int height = src.getHeight();
	    int width = src.getWidth();
	 
	    // scan through every pixel
	    for (int y = 0; y < height; y++)
	    {
	        for (int x = 0; x < width; x++)
	        {
	            // get one pixel
	            pixelColor = src.getPixel(x, y);
	            // saving alpha channel
	            A = Color.alpha(pixelColor);
	            // inverting byte for each R/G/B channel
	            R = 255 - Color.red(pixelColor);
	            G = 255 - Color.green(pixelColor);
	            B = 255 - Color.blue(pixelColor);
	            // set newly-inverted pixel to output image
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final bitmap
	    return bmOut;
	}
	
	
	public static Bitmap boost(Bitmap src, int type, float percent) {
	    int width = src.getWidth();
	    int height = src.getHeight();
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	 
	    int A, R, G, B;
	    int pixel;
	 
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            if(type == 1) {
	                R = (int)(R * (1 + percent));
	                if(R > 255) R = 255;
	            }
	            else if(type == 2) {
	                G = (int)(G * (1 + percent));
	                if(G > 255) G = 255;
	            }
	            else if(type == 3) {
	                B = (int)(B * (1 + percent));
	                if(B > 255) B = 255;
	            }
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	    return bmOut;
	}
	
	
	public static Bitmap doGreyscale(Bitmap src) {
	    // constant factors
	    final double GS_RED = 0.299;
	    final double GS_GREEN = 0.587;
	    final double GS_BLUE = 0.114;
	 
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	    // pixel information
	    int A, R, G, B;
	    int pixel;
	 
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	 
	    // scan through every single pixel
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get one pixel color
	            pixel = src.getPixel(x, y);
	            // retrieve color of all channels
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // take conversion up to one single value
	            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
	    // create output image
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // color information
	    int A, R, G, B;
	    int pixel;
	    // constant value curve
	    final int    MAX_SIZE = 256;
	    final double MAX_VALUE_DBL = 255.0;
	    final int    MAX_VALUE_INT = 255;
	    final double REVERSE = 1.0;
	 
	    // gamma arrays
	    int[] gammaR = new int[MAX_SIZE];
	    int[] gammaG = new int[MAX_SIZE];
	    int[] gammaB = new int[MAX_SIZE];
	 
	    // setting values for every gamma channels
	    for(int i = 0; i < MAX_SIZE; ++i) {
	        gammaR[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
	        gammaG[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
	        gammaB[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
	    }
	 
	    // apply gamma table
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            // look up gamma
	            R = gammaR[Color.red(pixel)];
	            G = gammaG[Color.green(pixel)];
	            B = gammaB[Color.blue(pixel)];
	            // set new color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	
	public static class ConvolutionMatrix
	{
	    public static final int SIZE = 3;
	 
	    public double[][] Matrix;
	    public double Factor = 1;
	    public double Offset = 1;
	 
	    public ConvolutionMatrix(int size) {
	        Matrix = new double[size][size];
	    }
	 
	    public void setAll(double value) {
	        for (int x = 0; x < SIZE; ++x) {
	            for (int y = 0; y < SIZE; ++y) {
	                Matrix[x][y] = value;
	            }
	        }
	    }
	 
	    public void applyConfig(double[][] config) {
	        for(int x = 0; x < SIZE; ++x) {
	            for(int y = 0; y < SIZE; ++y) {
	                Matrix[x][y] = config[x][y];
	            }
	        }
	    }
	 
	    public static Bitmap computeConvolution3x3(Bitmap src, ConvolutionMatrix matrix) {
	        int width = src.getWidth();
	        int height = src.getHeight();
	        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
	 
	        int A, R, G, B;
	        int sumR, sumG, sumB;
	        int[][] pixels = new int[SIZE][SIZE];
	 
	        for(int y = 0; y < height - 2; ++y) {
	            for(int x = 0; x < width - 2; ++x) {
	 
	                // get pixel matrix
	                for(int i = 0; i < SIZE; ++i) {
	                    for(int j = 0; j < SIZE; ++j) {
	                        pixels[i][j] = src.getPixel(x + i, y + j);
	                    }
	                }
	 
	                // get alpha of center pixel
	                A = Color.alpha(pixels[1][1]);
	 
	                // init color sum
	                sumR = sumG = sumB = 0;
	 
	                // get sum of RGB on matrix
	                for(int i = 0; i < SIZE; ++i) {
	                    for(int j = 0; j < SIZE; ++j) {
	                        sumR += (Color.red(pixels[i][j]) * matrix.Matrix[i][j]);
	                        sumG += (Color.green(pixels[i][j]) * matrix.Matrix[i][j]);
	                        sumB += (Color.blue(pixels[i][j]) * matrix.Matrix[i][j]);
	                    }
	                }
	 
	                // get final Red
	                R = (int)(sumR / matrix.Factor + matrix.Offset);
	                if(R < 0) { R = 0; }
	                else if(R > 255) { R = 255; }
	 
	                // get final Green
	                G = (int)(sumG / matrix.Factor + matrix.Offset);
	                if(G < 0) { G = 0; }
	                else if(G > 255) { G = 255; }
	 
	                // get final Blue
	                B = (int)(sumB / matrix.Factor + matrix.Offset);
	                if(B < 0) { B = 0; }
	                else if(B > 255) { B = 255; }
	 
	                // apply new pixel
	                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
	            }
	        }
	 
	        // final image
	        return result;
	    }
	}
	
	public static Bitmap engrave(Bitmap src) {
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.setAll(0);
	    convMatrix.Matrix[0][0] = -2;
	    convMatrix.Matrix[1][1] = 2;
	    convMatrix.Factor = 1;
	    convMatrix.Offset = 95;
	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	
	public static Bitmap smooth(Bitmap src, double value) {
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.setAll(1);
	    convMatrix.Matrix[1][1] = value;
	    convMatrix.Factor = value + 8;
	    convMatrix.Offset = 1;
	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	
	public static Bitmap sharpen(Bitmap src, double weight) {
	    double[][] SharpConfig = new double[][] {
	        { 0 , -2    , 0  },
	        { -2, weight, -2 },
	        { 0 , -2    , 0  }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(SharpConfig);
	    convMatrix.Factor = weight - 8;
	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	
	public static Bitmap applyMeanRemoval(Bitmap src) {
	    double[][] MeanRemovalConfig = new double[][] {
	        { -1 , -1, -1 },
	        { -1 ,  9, -1 },
	        { -1 , -1, -1 }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(MeanRemovalConfig);
	    convMatrix.Factor = 1;
	    convMatrix.Offset = 0;
	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	
	public static Bitmap applyGaussianBlur(Bitmap src) {
	    double[][] GaussianBlurConfig = new double[][] {
	        { 1, 2, 1 },
	        { 2, 4, 2 },
	        { 1, 2, 1 }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(GaussianBlurConfig);
	    convMatrix.Factor = 16;
	    convMatrix.Offset = 0;
	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	    
	    public static Bitmap applyBlackFilter(Bitmap source) {
	        // get image size
	        int width = source.getWidth();
	        int height = source.getHeight();
	        int[] pixels = new int[width * height];
	        // get pixel array from source
	        source.getPixels(pixels, 0, width, 0, 0, width, height);
	        // random object
	        Random random = new Random();
	     
	        int R, G, B, index = 0, thresHold = 0;
	        // iteration through pixels
	        for(int y = 0; y < height; ++y) {
	            for(int x = 0; x < width; ++x) {
	                // get current index in 2D-matrix
	                index = y * width + x;
	                // get color
	                R = Color.red(pixels[index]);
	                G = Color.green(pixels[index]);
	                B = Color.blue(pixels[index]);
	                // generate threshold
	                thresHold = random.nextInt(COLOR_MAX);
	                if(R < thresHold && G < thresHold && B < thresHold) {
	                    pixels[index] = Color.rgb(COLOR_MIN, COLOR_MIN, COLOR_MIN);
	                }
	            }
	        }
	        // output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	        return bmOut;
	    }
	    
//	    public static Bitmap createContrast(Bitmap src, double value) {
//	        // image size
//	        int width = src.getWidth();
//	        int height = src.getHeight();
//	        // create output bitmap
//	        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
//	        // color information
//	        int A, R, G, B;
//	        int pixel;
//	        // get contrast value
//	        double contrast = Math.pow((100 + value) / 100, 2);
//	     
//	        // scan through all pixels
//	        for(int x = 0; x < width; ++x) {
//	            for(int y = 0; y < height; ++y) {
//	                // get pixel color
//	                pixel = src.getPixel(x, y);
//	                A = Color.alpha(pixel);
//	                // apply filter contrast for every channel R, G, B
//	                R = Color.red(pixel);
//	                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
//	                if(R < 0) { R = 0; }
//	                else if(R > 255) { R = 255; }
//	     
//	                G = Color.red(pixel);
//	                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
//	                if(G < 0) { G = 0; }
//	                else if(G > 255) { G = 255; }
//	     
//	                B = Color.red(pixel);
//	                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
//	                if(B < 0) { B = 0; }
//	                else if(B > 255) { B = 255; }
//	     
//	                // set new pixel color to output bitmap
//	                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
//	            }
//	        }
//	     
//	        // return final image
//	        return bmOut;
//	    }
	    
	    public static Bitmap doBrightness(Bitmap src, int value) {
	        // image size
	        int width = src.getWidth();
	        int height = src.getHeight();
	        // create output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	        // color information
	        int A, R, G, B;
	        int pixel;
	     
	        // scan through all pixels
	        for(int x = 0; x < width; ++x) {
	            for(int y = 0; y < height; ++y) {
	                // get pixel color
	                pixel = src.getPixel(x, y);
	                A = Color.alpha(pixel);
	                R = Color.red(pixel);
	                G = Color.green(pixel);
	                B = Color.blue(pixel);
	     
	                // increase/decrease each channel
	                R += value;
	                if(R > 255) { R = 255; }
	                else if(R < 0) { R = 0; }
	     
	                G += value;
	                if(G > 255) { G = 255; }
	                else if(G < 0) { G = 0; }
	     
	                B += value;
	                if(B > 255) { B = 255; }
	                else if(B < 0) { B = 0; }
	     
	                // apply new pixel color to output bitmap
	                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	            }
	        }
	     
	        // return final image
	        return bmOut;
	    }
	    
	    
	    public static Bitmap decreaseColorDepth(Bitmap src, int bitOffset) {
	        // get image size
	        int width = src.getWidth();
	        int height = src.getHeight();
	        // create output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	        // color information
	        int A, R, G, B;
	        int pixel;
	     
	        // scan through all pixels
	        for(int x = 0; x < width; ++x) {
	            for(int y = 0; y < height; ++y) {
	                // get pixel color
	                pixel = src.getPixel(x, y);
	                A = Color.alpha(pixel);
	                R = Color.red(pixel);
	                G = Color.green(pixel);
	                B = Color.blue(pixel);
	     
	                // round-off color offset
	                R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
	                if(R < 0) { R = 0; }
	                G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
	                if(G < 0) { G = 0; }
	                B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
	                if(B < 0) { B = 0; }
	     
	                // set pixel color to output bitmap
	                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	            }
	        }
	     
	        // return final image
	        return bmOut;
	    }
	    
	    
	    public static Bitmap applyHueFilter(Bitmap source, int level) {
	        // get image size
	        int width = source.getWidth();
	        int height = source.getHeight();
	        int[] pixels = new int[width * height];
	        float[] HSV = new float[3];
	        // get pixel array from source
	        source.getPixels(pixels, 0, width, 0, 0, width, height);
	         
	        int index = 0;
	        // iteration through pixels
	        for(int y = 0; y < height; ++y) {
	            for(int x = 0; x < width; ++x) {
	                // get current index in 2D-matrix
	                index = y * width + x;              
	                // convert to HSV
	                Color.colorToHSV(pixels[index], HSV);
	                // increase Saturation level
	                HSV[0] *= level;
	                HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
	                // take color back
	                pixels[index] |= Color.HSVToColor(HSV);
	            }
	        }
	        // output bitmap                
	        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	        return bmOut;       
	    }
	    
	    public static Bitmap applySaturationFilter(Bitmap source, int level) {
	        // get image size
	        int width = source.getWidth();
	        int height = source.getHeight();
	        int[] pixels = new int[width * height];
	        float[] HSV = new float[3];
	        // get pixel array from source
	        source.getPixels(pixels, 0, width, 0, 0, width, height);
	     
	        int index = 0;
	        // iteration through pixels
	        for(int y = 0; y < height; ++y) {
	            for(int x = 0; x < width; ++x) {
	                // get current index in 2D-matrix
	                index = y * width + x;
	                // convert to HSV
	                Color.colorToHSV(pixels[index], HSV);
	                // increase Saturation level
	                HSV[1] *= level;
	                HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
	                // take color back
	                pixels[index] |= Color.HSVToColor(HSV);
	            }
	        }
	        // output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	        return bmOut;
	    }	
	    
	    public static Bitmap applyShadingFilter(Bitmap source, int shadingColor) {
	        // get image size
	        int width = source.getWidth();
	        int height = source.getHeight();
	        int[] pixels = new int[width * height];
	        // get pixel array from source
	        source.getPixels(pixels, 0, width, 0, 0, width, height);
	     
	        int index = 0;
	        // iteration through pixels
	        for(int y = 0; y < height; ++y) {
	            for(int x = 0; x < width; ++x) {
	                // get current index in 2D-matrix
	                index = y * width + x;
	                // AND
	                pixels[index] &= shadingColor;
	            }
	        }
	        // output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	        return bmOut;
	    }
	    
//	    public static final int COLOR_MIN = 0x00;
//	    public static final int COLOR_MAX = 0xFF;
//	     
//	    public static Bitmap applyFleaEffect(Bitmap source) {
//	        // get image size
//	        int width = source.getWidth();
//	        int height = source.getHeight();
//	        int[] pixels = new int[width * height];
//	        // get pixel array from source
//	        source.getPixels(pixels, 0, width, 0, 0, width, height);
//	        // a random object
//	        Random random = new Random();
//	     
//	        int index = 0;
//	        // iteration through pixels
//	        for(int y = 0; y < height; ++y) {
//	            for(int x = 0; x < width; ++x) {
//	                // get current index in 2D-matrix
//	                index = y * width + x;
//	                // get random color
//	                int randColor = Color.rgb(random.nextInt(COLOR_MAX),
//	                        random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
//	                // OR
//	                pixels[index] |= randColor;
//	            }
//	        }
//	        // output bitmap
//	        Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
//	        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
//	        return bmOut;
//	    }

	    // type definition
	    public static final int FLIP_VERTICAL = 1;
	    public static final int FLIP_HORIZONTAL = 2;
	     
	    public static Bitmap flip(Bitmap src, int type) {
	        // create new matrix for transformation
	        Matrix matrix = new Matrix();
	        // if vertical
	        if(type == FLIP_VERTICAL) {
	            // y = y * -1
	            matrix.preScale(1.0f, -1.0f);
	        }
	        // if horizonal
	        else if(type == FLIP_HORIZONTAL) {
	            // x = x * -1
	            matrix.preScale(-1.0f, 1.0f);
	        // unknown type
	        } else {
	            return null;
	        }
	     
	        // return transformed image
	        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	    }	 
	    
	    public static Bitmap rotate(Bitmap src, float degree) {
	        // create new matrix
	        Matrix matrix = new Matrix();
	        // setup rotation degree
	        matrix.postRotate(degree);
	     
	        // return new bitmap rotated using matrix
	        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	    }
	    
	    
	    
}
