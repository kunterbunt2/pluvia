//depthOfField.vs.glsl
#if __VERSION__ >= 130
out vec4 fragColor;
#define attribute in
#define varying out
#else
#define fragColor gl_FragColor
#endif
/*
#ifdef GL_ES
	#define PRECISION mediump
	precision PRECISION float;
	precision PRECISION int;
#else
	#define PRECISION
#endif
*/

attribute vec4 a_position;
attribute vec2 a_texCoord0;
varying vec2 v_texCoords;

void main() {
	v_texCoords = a_texCoord0;
	gl_Position = a_position;
}
