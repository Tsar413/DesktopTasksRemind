const { app, BrowserWindow, screen, ipcMain } = require('electron');
const path = require('path');

let mainWindow = null;

function injectDesktopChrome() {
  if (!mainWindow || mainWindow.isDestroyed()) return;

  const script = `
    (() => {
      if (document.getElementById('desktop-shell-bar')) return;

      const style = document.createElement('style');
      style.id = 'desktop-shell-style';
      style.textContent = "body {\
background: transparent !important;\
overflow: hidden !important;\
}\
.app-shell {\
background: rgba(255, 255, 255, 0.08) !important;\
border: 1px solid rgba(255, 255, 255, 0.12) !important;\
box-shadow: 0 14px 38px rgba(0,0,0,0.16) !important;\
}\
#desktop-shell-bar {\
position: fixed;\
top: 0;\
left: 0;\
right: 0;\
height: 42px;\
display: flex;\
align-items: center;\
justify-content: space-between;\
padding: 0 10px 0 14px;\
background: rgba(10, 14, 25, 0.10);\
backdrop-filter: blur(10px);\
-webkit-backdrop-filter: blur(10px);\
z-index: 99999;\
-webkit-app-region: drag;\
user-select: none;\
}\
#desktop-shell-title {\
color: rgba(255,255,255,0.92);\
font-size: 13px;\
font-weight: 600;\
letter-spacing: 0.2px;\
white-space: nowrap;\
overflow: hidden;\
text-overflow: ellipsis;\
}\
#desktop-shell-actions {\
display: flex;\
gap: 8px;\
-webkit-app-region: no-drag;\
}\
.desktop-shell-btn {\
width: 28px;\
height: 28px;\
border: none;\
border-radius: 8px;\
cursor: pointer;\
background: rgba(255,255,255,0.16);\
color: #fff;\
font-size: 14px;\
line-height: 1;\
}\
.desktop-shell-btn:hover {\
background: rgba(255,255,255,0.24);\
}\
.desktop-shell-btn.close:hover {\
background: rgba(239, 68, 68, 0.88);\
}\
.top-bar {\
padding-top: 52px !important;\
}";
      document.head.appendChild(style);

      const bar = document.createElement('div');
      bar.id = 'desktop-shell-bar';
      bar.innerHTML = '<div id="desktop-shell-title">桌面待办</div><div id="desktop-shell-actions"><button class="desktop-shell-btn" id="desktop-min-btn" title="最小化">—</button><button class="desktop-shell-btn close" id="desktop-close-btn" title="关闭">×</button></div>';
      document.body.appendChild(bar);

      const minBtn = document.getElementById('desktop-min-btn');
      const closeBtn = document.getElementById('desktop-close-btn');

      if (minBtn) {
        minBtn.addEventListener('click', () => {
          if (window.desktopShellAPI && window.desktopShellAPI.minimizeWindow) {
            window.desktopShellAPI.minimizeWindow();
          }
        });
      }

      if (closeBtn) {
        closeBtn.addEventListener('click', () => {
          if (window.desktopShellAPI && window.desktopShellAPI.closeWindow) {
            window.desktopShellAPI.closeWindow();
          }
        });
      }
    })();
  `;

  mainWindow.webContents.executeJavaScript(script).catch(() => {});
}

function createWindow() {
  const primaryDisplay = screen.getPrimaryDisplay();
  const { width } = primaryDisplay.workAreaSize;

  const winWidth = 430;
  const winHeight = 770;
  const marginRight = 20;
  const marginTop = 20;

  mainWindow = new BrowserWindow({
    width: winWidth,
    height: winHeight,
    x: width - winWidth - marginRight,
    y: marginTop,
    frame: false,
    transparent: true,
    opacity: 0.92,
    resizable: true,
    maximizable: false,
    minimizable: true,
    fullscreenable: false,
    alwaysOnTop: true,
    skipTaskbar: false,
    hasShadow: true,
    backgroundColor: '#00000000',
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false
    }
  });

  mainWindow.loadURL('http://localhost:8098/work');

  mainWindow.webContents.on('did-finish-load', () => {
    injectDesktopChrome();
  });

  mainWindow.webContents.on('did-fail-load', () => {
    mainWindow.loadURL(`data:text/html;charset=utf-8,
      <html>
        <body style="margin:0;font-family:Microsoft YaHei;background:rgba(20,20,20,.50);color:#fff;display:flex;align-items:center;justify-content:center;height:100vh;">
          <div style="width:82%;padding:24px;border-radius:18px;background:rgba(255,255,255,.10);backdrop-filter:blur(10px);text-align:center;">
            <div style="display:flex;justify-content:flex-end;margin-bottom:8px;">
              <button onclick="window.close()" style="width:28px;height:28px;border:none;border-radius:8px;background:rgba(255,255,255,.18);color:#fff;cursor:pointer;">×</button>
            </div>
            <h2 style="margin-top:0;">待办服务未启动</h2>
            <p>请先运行统一启动脚本，再打开桌面待办。</p>
          </div>
        </body>
      </html>`);
  });

  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

ipcMain.on('desktop:minimize', () => {
  if (mainWindow && !mainWindow.isDestroyed()) {
    mainWindow.minimize();
  }
});

ipcMain.on('desktop:close', () => {
  if (mainWindow && !mainWindow.isDestroyed()) {
    mainWindow.close();
  }
});

app.whenReady().then(() => {
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  app.quit();
});