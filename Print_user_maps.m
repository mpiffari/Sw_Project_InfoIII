close all
clearvars
clc
x = [6
65
8
43
8
34
10
1
77
21
21
45
1
42
23
65
45
9
74
-20
10
100
1];
y = [3
41
4
3
4
5
4
30
89
100
12
7
65
39
38
19
17
54
63
-20
40
20
1];
raggi = [5
8
32
2
3
9
3
21
3
9
20
14
1
7
33
6
12
25
35
15
22
30
12];
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
    xlim([-50 120])
    ylim([-50 120])

    % Set the axis aspect ratio to 1:1.
    axis square

    % Set a title.
    title(['k = ' num2str(k)])

    % Display the circles.
    viscircles(centers,radii,'Color',colors{k}, 'LineStyle','-','LineWidth',0.5);
    hold on
    scatter(x(:), y(:),5,'k','+')
    % Pause for 1 second.
    pause(1)
end
