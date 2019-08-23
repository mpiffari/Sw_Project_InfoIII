close all
clear all
clc
x = [1
    6
8
43
8
34
45
10
56
1
65
77
21
65]
y = [1
3
4
3
4
5
6
8
4
30
8
59
76
100
105];
raggi = [1
5
45
2
3
9
3
40
3
9
8];
% 
% [x y]
% %scatter(x(:), y(:), raggi(:).^2)
% viscircles([x y],raggi)

figure
colors = {'b','r','g','y','k'};

for k = 1:1
    % Create 5 random circles to display,
    X = x
    Y = y
    centers = [X Y];
    radii = raggi;

    % Clear the axes.
    cla

    % Fix the axis limits.
    xlim([-120 120])
    ylim([-120 120])

    % Set the axis aspect ratio to 1:1.
    axis square

    % Set a title.
    title(['k = ' num2str(k)])

    % Display the circles.
    viscircles(centers,radii,'Color',colors{k});
    hold on
    scatter(x(:), y(:),5,'k','+')
    % Pause for 1 second.
    pause(1)
end
